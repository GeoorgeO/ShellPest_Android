USE [ShellPest]
GO
/****** Object:  StoredProcedure [dbo].[SP_BSC_ClienteGeneral]    Script Date: 25/08/2018 12:40:29 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF  EXISTS (SELECT * FROM SYS.OBJECTS WHERE TYPE = 'P' AND NAME = 'SP_Cosecha_Insert')
DROP PROCEDURE SP_Cosecha_Insert
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
create PROCEDURE [dbo].[SP_Cosecha_Insert] 
	-- Add the parameters for the stored procedure here
	@Fecha datetime,
    @Id_bloque char(4),
    @BICO varchar(50),
    @Cajas_Cosecha numeric(10,0),
	@Cajas_Desecho numeric(10,0),
	@Cajas_Pepena numeric(10,0),
	@Cajas_RDiaria numeric(10,0),
    @Id_Usuario varchar(10),
    @F_Fecha_Crea datetime,
    @c_codigo_eps char(2)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	declare @correcto bit

	begin transaction T1;
	begin try


		declare @Existe int
		select @Existe = count(Id_Bloque) from dbo.t_Cosecha a where (a.Fecha=@Fecha and a.Id_bloque=@Id_bloque and a.c_codigo_eps=@c_codigo_eps)

		if @Existe>0 
		
			UPDATE dbo.t_Cosecha
		        SET BICO=@BICO,
				Cajas_Cosecha=@Cajas_Cosecha,
				Cajas_Desecho=@Cajas_Desecho,
				Cajas_Pepena=@Cajas_Pepena,
				Cajas_RDiaria=@Cajas_RDiaria
		    WHERE
		    	Fecha=@Fecha
				and Id_bloque=@Id_bloque
				and c_codigo_eps=@c_codigo_eps
				
		else
			INSERT INTO dbo.t_Cosecha
	           (Fecha
	           ,Id_bloque
			   ,c_codigo_eps
			   ,BICO
			   ,Cajas_Cosecha
			   ,Cajas_Desecho
			   ,Cajas_Pepena
			   ,Cajas_RDiaria
			   ,Id_Usuario
	           ,F_Fecha_Crea )
	     	VALUES
	           (@Fecha
	           ,@Id_bloque
			   ,@c_codigo_eps
			   ,@BICO
			   ,@Cajas_Cosecha
			   ,@Cajas_Desecho
			   ,@Cajas_Pepena
			   ,@Cajas_RDiaria
			   ,@Id_Usuario
	           ,@F_Fecha_Crea
			   )
		
		commit transaction T1;
		set @correcto=1
	end try
	begin catch
		rollback transaction T1;
		set @correcto=0
	end catch

	select @correcto resultado
END
